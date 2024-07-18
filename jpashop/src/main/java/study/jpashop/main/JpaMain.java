package study.jpashop.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		
		// EntityManagerFactory는 하나만 생성해서 애플리케이션 전체에서 공유 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		// EntityManager는 스레드간 공유 X(사용하고 버려야 함)
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		et.begin();
		
		try {
			
			et.commit();
			
		} catch (Exception e) {
			
			et.rollback();
			
		} finally {
			
			em.close();
			emf.close();
			
		}
		
	}
	
}
