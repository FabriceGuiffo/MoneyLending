package com.urgent2k.exercice1.DAL;

import com.urgent2k.exercice1.Model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,Integer> {
}
