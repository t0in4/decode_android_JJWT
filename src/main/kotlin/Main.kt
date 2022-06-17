import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.*

fun main(args: Array<String>) {
    val a = getJWT()
    println("encoded token $a")
    val b = decodeJWT(a)
    println("decoded token $b")

    val android_jwt = "eyJraWQiOiJhamU5MmRwYzR0NHR1N2JidTh1biIsInR5cCI6IkpXVCIsImFsZyI6IlBTMjU2In0.eyJpc3MiOiJhamVpanAxOW01OTlzOGFvczZ2ciIsImF1ZCI6Imh0dHBzOlwvXC9pYW0uYXBpLmNsb3VkLnlhbmRleC5uZXRcL2lhbVwvdjFcL3Rva2VucyIsImlhdCI6MTY1NTQ1MDQ5NCwiZXhwIjoxNjU1NDUwODU0fQ.Swv4MSF7rdfmg3alTmHSnDtt7v-xZjSMLepIvAih-WFtMTKp6uZZWSCF19D1nOuEmlvtBPWT-6UEzw4sH-OcxW5wYu-GHDpEA2jOnp8BIjKFV_ZnmOdy8zV2ue-waZltmvytNQHBUBwfkVGo09-UGpx4KCm6mLCAiMTEuS6Xbsp4wDUVBC8-lhvYw5e9e51JuFclytjQoxfGetI7CuZw7supF-1Dds_zwEPzUpUbawboQiEwkKfN1dmSq-CdmrWX38hXUhIgoaUBmzJucAGZ-fcMa8N1GgqqnCfDuBBQZdq6BpCkAZBVczZTV6OBiEIu67hjRKFp7XFAolDHitkIoA"
    val c = decodeJWT(android_jwt)
    println("Android decoded token $c")


}
fun getJWT(): String {

    lateinit var privateKeyPem: PemObject

    val file = File("src/main/kotlin/yapem.pem").inputStream()
    val isr = InputStreamReader(file)
    val readerBufferedFile = BufferedReader(isr)

    val reader: PemReader = PemReader(readerBufferedFile)
    privateKeyPem = reader.readPemObject()


    val keyFactory = KeyFactory.getInstance("RSA")
    val privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyPem.getContent()))
    val serviceAccountId = "ajeijp19m599s8aos6vr"
    val keyId = "aje92dpc4t4tu7bbu8un"
    val now = Instant.now()

    //  JWT.
    val encodedToken: String = Jwts.builder()
        .setHeaderParam("kid", keyId)
        .setIssuer(serviceAccountId)
        .setAudience("https://iam.api.cloud.yandex.net/iam/v1/tokens")
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(360)))
        .signWith(
            privateKey,
            SignatureAlgorithm.PS256
        )
        .compact()

    return encodedToken
}

fun decodeJWT(encodedToken: String) {
    lateinit var publicKeyPem: PemObject
    val file = File("src/main/kotlin/open.pem").inputStream()
    val isr = InputStreamReader(file)
    val readerBufferedFile = BufferedReader(isr)
    val reader: PemReader = PemReader(readerBufferedFile)
    publicKeyPem = reader.readPemObject()
    val keyFactory = KeyFactory.getInstance("RSA")
    val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyPem.getContent()))
    //Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys
    //generatePrivate(PKCS8EncodedKeySpec(publicKeyPem.getContent()))
    var a =  Jwts.parserBuilder()
       .setSigningKey(publicKey)
       .build()
       .parse(encodedToken)
    println(a)
}