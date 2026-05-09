package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.entity.TuVan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TuVanRepository extends JpaRepository<TuVan, Integer> {

    List<TuVan> findByUser_IdNguoiDungOrderByNgayGuiDesc(Integer idNguoiDung);
}