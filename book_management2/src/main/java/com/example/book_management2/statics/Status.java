package com.example.book_management2.statics;

public enum Status {
    NEW("Mới"), LIKE_NEW("Gần mới"), VERY_GOOD("Rất tốt"), GOOD("Tốt"), ACCEPTABLE("Chấp nhận được");

    String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
