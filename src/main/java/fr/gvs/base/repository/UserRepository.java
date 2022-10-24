package fr.gvs.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.gvs.base.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
