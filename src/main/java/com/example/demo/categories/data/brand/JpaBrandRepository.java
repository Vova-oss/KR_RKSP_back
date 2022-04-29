package com.example.demo.categories.data.brand;

import com.example.demo.categories.data.type.TypeDataMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBrandRepository extends JpaRepository<BrandDataMapper, Long> {

    BrandDataMapper findByNameAndTypeDataMapper(String name, TypeDataMapper typeId);

}
