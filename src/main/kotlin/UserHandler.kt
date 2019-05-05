package space.anity

import io.javalin.*
import io.javalin.rendering.template.TemplateUtil.model
import org.joda.time.*
import java.util.logging.*
import kotlin.math.*

class UserHandler {
    private val log = Logger.getLogger(this.javaClass.name)

    /**
     * Renders the login page
     */
    fun renderLogin(ctx: Context) {
        if (userHandler.getVerifiedUserId(ctx) > 0 || !databaseController.isSetup()) ctx.redirect("/")
        else ctx.render("login.rocker.html", model("message", "", "counter", 0))
    }

    /**
     * Checks and verifies users credentials and logs the user in
     */
    fun login(ctx: Context) {
        if (getVerifiedUserId(ctx) > 0 || !databaseController.isSetup()) ctx.redirect("/")

        val username = ctx.formParam("username").toString()
        val password = ctx.formParam("password").toString()
        val requestIp = ctx.ip()

        val loginAttempts = databaseController.getLoginAttempts(requestIp)
        val lastAttemptDifference =
            if (loginAttempts.isEmpty()) -1
            else Interval(
                loginAttempts[loginAttempts.indexOfLast { true }].first.toInstant(),
                Instant()
            ).toDuration().standardSeconds.toInt()

        var lastHourAttempts = 0
        loginAttempts.forEach {
            val difference = Interval(it.first.toInstant(), Instant()).toDuration().standardMinutes.toInt()
            if (difference < 60) lastHourAttempts += 1
        }
        val nextThreshold = 4f.pow(lastHourAttempts + 1)

        if (lastAttemptDifference > 4f.pow(lastHourAttempts) || lastHourAttempts == 0) {
            if (databaseController.checkUser(username, password)) {
                ctx.cookieStore("verification", databaseController.getVerificationId(username))
                ctx.cookieStore("userId", databaseController.getUserId(username))
                ctx.redirect("/")
            } else {
                databaseController.loginAttempt(DateTime(), requestIp)
                ctx.render(
                    "login.rocker.html",
                    model(
                        "message",
                        "Login failed!",
                        "counter", if (nextThreshold / 60 > 60) 3600 else nextThreshold.toInt()
                    )
                )
            }
        } else {
            databaseController.loginAttempt(DateTime(), requestIp)
            ctx.render(
                "login.rocker.html",
                model(
                    "message",
                    "Too many request.",
                    "counter", if (nextThreshold / 60 > 60) 3600 else nextThreshold.toInt()
                )
            )
        }
    }

    /**
     * Logs the user out of the system
     */
    fun logout(ctx: Context) {
        ctx.clearCookieStore()
        ctx.removeCookie("javalin-cookie-store", "/")
        ctx.redirect("/")
    }

    /**
     * Renders the admin interface
     */
    fun renderAdmin(ctx: Context) {
        ctx.render("admin.rocker.html", model("message", ""))
    }

    /**
     * Renders the setup page
     */
    fun renderSetup(ctx: Context) {
        if (databaseController.isSetup()) ctx.redirect("/user/login")
        else ctx.render("setup.rocker.html", model("message", ""))
    }

    /**
     * Sets up the general settings and admin credentials
     */
    fun setup(ctx: Context) {
        try {
            val username = ctx.formParam("username").toString()
            val password = ctx.formParam("password").toString()
            val verifyPassword = ctx.formParam("verifyPassword").toString()
            if (password == verifyPassword) {
                if (databaseController.createUser(username, password, "ADMIN")) {
                    databaseController.toggleSetup()
                    ctx.redirect("/user/login")
                } else ctx.status(400).render("setup.rocker.html", model("message", "User already exists!"))
            } else ctx.status(400).render("setup.rocker.html", model("message", "Passwords do not match!"))
        } catch (_: Exception) {
            ctx.status(400).render("setup.rocker.html", model("message", "An error occurred!"))
        }
    }

    /**
     * Renders the registration page
     */
    fun renderRegistration(ctx: Context) {
        val username = ctx.queryParam("username", "")
        val token = ctx.queryParam("token", "")

        if (username.isNullOrEmpty()) ctx.status(403).result("Please provide a valid username!")
        else if (token.isNullOrEmpty()) ctx.status(403).result("Please provide a valid token!")
        else {
            if (databaseController.isUserRegistrationValid(username, token))
                ctx.render("register.rocker.html", model("username", username, "token", token, "message", ""))
            else ctx.redirect("/user/login")
        }
    }

    /**
     * Registers a new user
     */
    fun register(ctx: Context) {
        try {
            val username = ctx.formParam("username").toString()
            val token = ctx.formParam("token").toString()
            val password = ctx.formParam("password").toString()
            val verifyPassword = ctx.formParam("verifyPassword").toString()

            if (password == verifyPassword) {
                if (databaseController.isUserRegistrationValid(username, token)) {
                    databaseController.createUser(username, password, "USER")
                    databaseController.removeRegistrationIndex(username)
                    ctx.redirect("/user/login")
                } else ctx.render(
                    "register.rocker.html",
                    model("username", username, "token", token, "message", "Not authorized!")
                )
            } else ctx.render(
                "register.rocker.html",
                model("username", username, "token", token, "message", "The passwords don't match!")
            )
        } catch (_: Exception) {
            ctx.status(400).result("An exception occurred.")
        }
    }

    /**
     * Gets the username and verifies its identity
     */
    fun getVerifiedUserId(ctx: Context): Int {
        return if (databaseController.getUserIdByVerificationId(ctx.cookieStore("verification") ?: "verification")
            == ctx.cookieStore("userId") ?: "userId"
        ) ctx.cookieStore("userId")
        else -1
    }

    /**
     * Checks whether a user has admin privileges
     */
    fun isAdmin(usernameString: String): Boolean {
        val userId = databaseController.getUserId(usernameString)
        return if (userId > 0) databaseController.getRoles(userId).contains(Roles.ADMIN)
        else false
    }
}
