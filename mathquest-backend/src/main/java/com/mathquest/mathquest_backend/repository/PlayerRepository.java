package com.mathquest.mathquest_backend.java.repository;

import com.mathquest.mathquest_backend.java.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
