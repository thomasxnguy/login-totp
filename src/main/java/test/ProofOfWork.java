package test;


import lombok.Builder;
import lombok.Value;
import lombok.experimental.UtilityClass;
import lombok.experimental.var;
import lombok.val;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class ProofOfWork {

    private final long X64_128_C1 = 0x87c37b91114253d5L;
    private final long X64_128_C2 = 0x4cf5ad432745937fL;

    // Corpus of usable characters. Note there are 62 of them so we pad the end with two repeated
    // values in order to get a nice power of two.
    private final byte[] CORPUS = {
            0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50,
            0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66,
            0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76,
            0x77, 0x78, 0x79, 0x7a, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x41, 0x42
    };

    @Value
    @Builder(builderClassName = "Builder")
    public static class Result {

        long elapsedMillis;
        long attemptsPerSecond;
        String guess;
    }

    public static Result solve(
            final String key,
            final String mask,
            final long seed,
            final long timeout,
            final TimeUnit timeUnit) {

        // Prepare stats
        val startTime = System.nanoTime();
        val maxDuration = TimeUnit.NANOSECONDS.convert(timeout, timeUnit);
        var attempts = 0L;

        // Prepare buffer
        val prefix = key.getBytes(Charset.forName("US-ASCII"));
        val prefixLength = prefix.length;
        val bytes = new byte[16];
        System.arraycopy(prefix, 0, bytes, 0, prefixLength);

        // Start
        val m = Long.valueOf(mask, 16);
        var r = new Random().nextLong();
        while (System.nanoTime() - startTime < maxDuration) {
            ++attempts;

            for (var i = prefixLength; i < 16; ++i) {
                // Quick LCG implementation (inlined)
                // It gives 48-bit random numbers, but our corpus indices are 6-bit so we shift right by 42
                bytes[i] = CORPUS[(int) ((r = (r * 0x5DEECE66DL + 0xBL) & 0xffffffffffffL) >>> 42)];
            }

            // Murmur3, abridged version for 16-byte payload
            var h1 = seed;
            var h2 = seed;

            var k1 = (long) (bytes[7] & 0xff);
            k1 = (k1 << 8) | (long) (bytes[6] & 0xff);
            k1 = (k1 << 8) | (long) (bytes[5] & 0xff);
            k1 = (k1 << 8) | (long) (bytes[4] & 0xff);
            k1 = (k1 << 8) | (long) (bytes[3] & 0xff);
            k1 = (k1 << 8) | (long) (bytes[2] & 0xff);
            k1 = (k1 << 8) | (long) (bytes[1] & 0xff);
            k1 = (k1 << 8) | (long) (bytes[0] & 0xff);

            h1 ^= mixK1(k1);

            h1 = Long.rotateLeft(h1, 27);
            h1 += h2;
            h1 = h1 * 5 + 0x52dce729;

            var k2 = (long) (bytes[15] & 0xff);
            k2 = (k2 << 8) | (long) (bytes[14] & 0xff);
            k2 = (k2 << 8) | (long) (bytes[13] & 0xff);
            k2 = (k2 << 8) | (long) (bytes[12] & 0xff);
            k2 = (k2 << 8) | (long) (bytes[11] & 0xff);
            k2 = (k2 << 8) | (long) (bytes[10] & 0xff);
            k2 = (k2 << 8) | (long) (bytes[9] & 0xff);
            k2 = (k2 << 8) | (long) (bytes[8] & 0xff);
            h2 ^= mixK2(k2);

            h2 = Long.rotateLeft(h2, 31);
            h2 += h1;
            h2 = h2 * 5 + 0x38495ab5;

            h1 ^= 16;
            h2 ^= 16;

            h1 += h2;
            h2 += h1;

            if (m == ((fmix64(h1) + fmix64(h2)) >>> 48L)) {
                // Success
                val elapsed = System.nanoTime() - startTime;
                return Result.builder()
                        .guess(new String(bytes, Charset.forName("US-ASCII")))
                        .elapsedMillis(TimeUnit.NANOSECONDS.toMillis(elapsed))
                        .attemptsPerSecond((attempts * TimeUnit.SECONDS.toNanos(1)) / elapsed)
                        .build();
            }
        }
        return null;
    }

    private long mixK1(long k1) {
        k1 *= X64_128_C1;
        k1 = Long.rotateLeft(k1, 31);
        k1 *= X64_128_C2;

        return k1;
    }

    private long mixK2(long k2) {
        k2 *= X64_128_C2;
        k2 = Long.rotateLeft(k2, 33);
        k2 *= X64_128_C1;

        return k2;
    }

    private long fmix64(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;

        return k;
    }


    public static void main(String[] args) {
        Result r = ProofOfWork.solve("b6", "0da8", 1308262750, 10L, TimeUnit.MINUTES);

        System.out.println(r.guess);
        System.out.println(r.elapsedMillis);
        System.out.println(r.attemptsPerSecond);

        for(int i=0;i<30;i++) {
            long d = new Random().nextLong();
            d = (((d * 0x5DEECE66DL + 0xBL) & 0xffffffffffffL) >>> 42);
            System.out.println(d);
        }

    }
}