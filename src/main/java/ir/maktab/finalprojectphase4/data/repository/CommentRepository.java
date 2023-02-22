package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.model.Comment;
import ir.maktab.finalprojectphase4.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.isDeleted= :isDeleted")
    List<Comment> findAllByDeletedIs(boolean isDeleted);

    @Query("select c from Comment c where c.expert= :expert")
    List<Comment> findByExpert(Expert expert);

}
