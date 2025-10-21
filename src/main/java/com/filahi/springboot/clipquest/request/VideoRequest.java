package com.filahi.springboot.clipquest.request;


import jakarta.validation.constraints.NotEmpty;

public record VideoRequest(
        @NotEmpty(message = "Title is mandatory")
        String title,

        @NotEmpty(message = "Description is mandatory")
        String description

//        @NotEmpty(message = "FilePath is mandatory")
//        String filePath
) {
}