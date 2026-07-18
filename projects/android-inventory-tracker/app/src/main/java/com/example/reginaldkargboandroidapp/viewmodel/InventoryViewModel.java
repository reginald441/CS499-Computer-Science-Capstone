package com.example.reginaldkargboandroidapp.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.reginaldkargboandroidapp.data.InventoryRepository;
import com.example.reginaldkargboandroidapp.model.InventoryItem;
import com.example.reginaldkargboandroidapp.util.ValidationUtils;
import java.util.Collections;
import java.util.List;

/** Presentation logic for inventory CRUD operations. */
public class InventoryViewModel extends ViewModel {
    public static class Result {
        public final boolean success; public final String message;
        private Result(boolean success, String message) { this.success=success; this.message=message; }
        public static Result ok(String message) { return new Result(true, message); }
        public static Result fail(String message) { return new Result(false, message); }
    }

    private InventoryRepository repository;
    private String owner;
    public void initialize(InventoryRepository repository, String owner) { this.repository=repository; this.owner=owner; }
    public List<InventoryItem> items() { return repository == null ? Collections.emptyList() : repository.getForUser(owner); }

    public Result add(String name, String quantityText, String location) {
        String error = ValidationUtils.validateItem(name, quantityText, location);
        if (error != null) return Result.fail(error);
        boolean ok = repository.add(owner, name, Integer.parseInt(quantityText.trim()), location);
        return ok ? Result.ok("Item added.") : Result.fail("The item could not be added.");
    }

    public Result update(long id, String name, String quantityText, String location) {
        String error = ValidationUtils.validateItem(name, quantityText, location);
        if (error != null) return Result.fail(error);
        boolean ok = repository.update(id, owner, name, Integer.parseInt(quantityText.trim()), location);
        return ok ? Result.ok("Item updated.") : Result.fail("The item could not be updated.");
    }

    public Result delete(long id) {
        return repository.delete(id, owner) ? Result.ok("Item deleted.") : Result.fail("The item could not be deleted.");
    }
}
