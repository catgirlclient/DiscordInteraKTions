package live.shuuyu.discordinteraktions.verifier

import java.math.BigInteger
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.EdECPoint
import java.security.spec.EdECPublicKeySpec
import java.security.spec.NamedParameterSpec
import kotlin.experimental.and

public class InteractionRequestVerifier(publicKey: String) {
    public companion object {
        /**
         * The algorithm used in Discord's interactions requests
         */
        private val INTERACTIONS_ALGORITHM = "ed25519"
        private val kf = KeyFactory.getInstance(INTERACTIONS_ALGORITHM)
    }
    public val signingKey: EdECPublicKeySpec = generateKeySpec(hex(publicKey))
    private val generatedPublicKey = kf.generatePublic(signingKey)

    /**
     * Validates the request with [requestBody], [signature] and [timestamp] and returns if the request is valid or not
     *
     * @param requestBody The requests' POST body
     * @param signature   From the `X-Signature-Ed25519` header
     * @param timestamp   From the `X-Signature-Timestamp` header
     * @return if the request is valid
     */
    public fun verifyKey(requestBody: String, signature: String, timestamp: String): Boolean {
        val signedData = Signature.getInstance(INTERACTIONS_ALGORITHM)
        signedData.initVerify(generatedPublicKey)

        signedData.update((timestamp + requestBody).toByteArray())
        return signedData.verify(hex(signature))
    }

    /**
     * Decode bytes from HEX string. It should be no spaces and `0x` prefixes.
     */
    private fun hex(s: String): ByteArray {
        // From Ktor "Crypto.kt" file
        val result = ByteArray(s.length / 2)
        for (idx in result.indices) {
            val srcIdx = idx * 2
            val high = s[srcIdx].toString().toInt(16) shl 4
            val low = s[srcIdx + 1].toString().toInt(16)
            result[idx] = (high or low).toByte()
        }

        return result
    }

    // https://stackoverflow.com/questions/65780235/ed25519-in-jdk-15-parse-public-key-from-byte-array-and-verify
    private fun generateKeySpec(publicKeyAsByteArray: ByteArray): EdECPublicKeySpec {
        // Key is already converted from hex string to a byte array. (with the hex(...) method)
        val byteArray = publicKeyAsByteArray.apply {
            // Make sure most significant bit will be 0 - after reversing.
            this[size - 1] = this[size - 1] and 127
        }.reversedArray()

        // Determine if x was odd.
        val xisodd = byteArray[byteArray.size - 1].toInt() and 255 shr 7 == 1

        val y = BigInteger(1, byteArray)
        val paramSpec = NamedParameterSpec(INTERACTIONS_ALGORITHM)
        val ep = EdECPoint(xisodd, y)

        return EdECPublicKeySpec(paramSpec, ep)
    }
}
