package com.demo.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Author ID is required")
    private Long authorId;

    public CreateBookRequest() {}
    public CreateBookRequest(String title, Long authorId) {
        this.title = title;
        this.authorId = authorId;
    }
}
