package com.sprylab.purple.example.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.sprylab.purple.example.model.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestController
class UsernamePasswordController {

    companion object {
        const val ISSUER = "generic-entitlement-sample"
    }

    private val algorithm = Algorithm.HMAC256("OdKb0D395Dl1XLl204i33wL2T6Ve7O0s")
    private val verifier = JWT.require(algorithm).withIssuer(ISSUER).build()

    @PostMapping("/v1/login")
    fun login(
        @RequestBody
        request: GenericLoginRequest
    ): GenericLoginResponse {
        val (appId, deviceId, username, password) = request
        // Check parameters against user database
        return if (username == "user@example.com" && password == "example") {
            // Valid credentials, generate access token
            val accessToken = createAccessToken(username)
            // Return access token
            GenericLoginResponse(accessToken, username)
        } else {
            // Return error
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials")
        }
    }

    @PostMapping("/v1/verify")
    fun verify(
        @RequestBody
        request: GenericVerifyTokenRequest
    ): GenericVerifyTokenResponse = try {
        val (appId, deviceId, accessToken) = request
        // Verify access token
        val verifiedToken = verifier.verify(accessToken)
        // Generate new access token
        val rotatedAccessToken = createAccessToken(verifiedToken.subject)
        // return updated token
        GenericVerifyTokenResponse(
            accessToken = rotatedAccessToken,
            userId = verifiedToken.subject
        )
    } catch (e: JWTVerificationException) {
        // Return error
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials")
    }

    @GetMapping("/v1/entitlements")
    fun entitlements(
        @RequestParam appId: String,
        @RequestParam(required = false) deviceId: String?,
        @RequestParam accessToken: String,
    ): List<UserEntitlement> = try {
        // Verify access token
        val verifiedToken = verifier.verify(accessToken)
        // return updated token
        listOf(
            UniversalEntitlement(appId)
        )
    } catch (e: JWTVerificationException) {
        // Return error
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials")
    }

    @PostMapping("/v1/logout")
    fun logout(
        @RequestBody request: GenericLogoutRequest
    ) = try {
        val (appId, deviceId, accessToken) = request
        // Verify access token
        verifier.verify(accessToken)
        // return nothing
    } catch (e: JWTVerificationException) {
        // Return error
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials")
    }

    @ExceptionHandler(ResponseStatusException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun responseToBody(e: ResponseStatusException): GenericEntitlementError {
        return GenericEntitlementError(
            code = EntitlementErrorCode.AUTHENTICATION_ERROR,
            message = e.reason ?: "Invalid credentials"
        )
    }

    private fun createAccessToken(username: String): String = JWT.create()
        .withIssuer(ISSUER)
        .withSubject(username)
        .withExpiresAt(Instant.now().plusSeconds(3600)) // 1 hour
        .sign(algorithm)

}
