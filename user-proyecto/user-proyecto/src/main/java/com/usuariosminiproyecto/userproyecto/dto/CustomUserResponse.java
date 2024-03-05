package com.usuariosminiproyecto.userproyecto.dto;
import java.util.List;

public class CustomUserResponse {

    private List<UserDTO> users;
    private int totalPages;
    private long totalUsers;
    private int pageSize;

    public CustomUserResponse(List<UserDTO> users, int totalPages, long totalUsers, int pageSize) {
        this.users = users;
        this.totalPages = totalPages;
        this.totalUsers = totalUsers;
        this.pageSize = pageSize;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}