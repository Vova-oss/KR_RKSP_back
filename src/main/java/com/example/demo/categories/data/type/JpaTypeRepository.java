package com.example.demo.categories.data.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTypeRepository extends JpaRepository<TypeDataMapper, Long> {

    TypeDataMapper findByName(String name);
    Boolean existsByName(String name);


}
