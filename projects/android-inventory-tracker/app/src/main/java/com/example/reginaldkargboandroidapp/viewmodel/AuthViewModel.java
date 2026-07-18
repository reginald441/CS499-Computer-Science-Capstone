package com.example.reginaldkargboandroidapp.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.reginaldkargboandroidapp.data.AuthRepository;
import com.example.reginaldkargboandroidapp.model.User;
import com.example.reginaldkargboandroidapp.util.ValidationUtils;

/** Presentation logic for login and account creation. */
public class AuthViewModel extends ViewModel {
    public static class Result {
        public final boolean success; public final String message; public final User user;
        private Result(boolean success, String message, User user) { this.success=success; this.message=message; this.user=user; }
        public static Result ok(String message, User user) { return new Result(true, message, user); }
        public static Result fail(String message) { return new Result(false, message, null); }
    }

    private AuthRepository repository;
    public void initialize(AuthRepository repository) { if (this.repository == null) this.repository = repository; }

    public Result login(String username, String password) {
        String error = ValidationUtils.validateUsername(username);
        if (error != null) return Result.fail(error);
        if (password == null || password.isEmpty()) return Result.fail("Password is required.");
        User user = repository.authenticate(username, password);
        return user == null ? Result.fail("The username or password is incorrect.") : Result.ok("Login successful.", user);
    }

    public Result createAccount(String username, String password) {
        String error = ValidationUtils.validateUsername(username);
        if (error != null) return Result.fail(error);
        error = ValidationUtils.validatePassword(password);
        if (error != null) return Result.fail(error);
        return repository.createUser(username, password)
                ? Result.ok("Account created. Please log in.", null)
                : Result.fail("That username already exists.");
    }
}
