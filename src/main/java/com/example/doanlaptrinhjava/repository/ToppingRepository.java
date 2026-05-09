package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToppingRepository
        extends JpaRepository<Topping, Integer> {

}