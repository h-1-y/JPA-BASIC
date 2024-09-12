package hellojpa;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hellojpa.item.Album;
import hellojpa.item.Item;
import hellojpa.item.Movie;

public class JpaMain {

	public static void main(String[] args) {
		
		// EntityManagerFactory는 하나만 생성해서 애플리케이션 전체에서 공유 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		// EntityManager는 스레드간 공유 X(사용하고 버려야 함)
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		et.begin();
		
		try {
			
			Team team1 = new Team();
			team1.setName("Team_A");
			team1.setInUser("ADMIN");
			team1.setInDate(LocalDateTime.now());
			
			Team team2 = new Team();
			team2.setName("Team_B");
			team2.setInUser("ADMIN");
			team2.setInDate(LocalDateTime.now());
			
			em.persist(team1);
			em.persist(team2);
			
			Locker locker = new Locker();
			locker.setName("locker 1");
			
			Locker locker2 = new Locker();
			locker2.setName("locker 2");
			
			em.persist(locker);
			em.persist(locker2);
			
			Member member = new Member();
			
			member.setName("KIM");
			// 1차 캐시에 저장된 team에서 ID를 꺼내 set
//			member.setTeamId(team.getId()); 
			member.changeTeam(team1);
			member.setLocker(locker);
			member.setInUser("KIM");
			member.setInDate(LocalDateTime.now());
			
			Member member2 = new Member();
			member2.setName("HAN");
			member2.changeTeam(team2);
			member2.setLocker(locker2);
			member2.setInUser("HAN");
			member2.setInDate(LocalDateTime.now());
			
			em.persist(member);
			em.persist(member2);
			
//			em.flush();
//			em.clear();
			
			// 현재는 연관관계 매핑이 돼 있지 않기 때문에 Member를 먼저 조회 후 
			// 조회된 Member의 TeamId를 통해 Team 객체 조회 
			// 객체지향적이지 못하다. 
//			Member getMember = em.find(Member.class, member.getId());
//			Team getTeam = em.find(Team.class, getMember.getTeamId());
//			Team getTeam = getMember.getTeam();
//			List<Member> getMembers = getMember.getTeam().getMembers();
			
//			for ( Member m : getMembers ) 
//				System.out.println(m.getName());
			
			Movie movie = new Movie();
			
			movie.setDirector("AAA");
			movie.setActor("BBB");
			movie.setName("영화제목입니당");
			movie.setPrice(15000);
			
			em.persist(movie);

			Album album = new Album();
			
			album.setArtist("AAA");
			album.setName("앨범임당");
			album.setPrice(10000);
			
			em.persist(album);
			
			em.flush();
			em.clear();
			
			// em.find( ~~ )
			// Member를 조회하지만 Member와 연관된 Team, Locker 모두 조인하여 가지고 온다.
//			Member findMember = em.find(Member.class, member.getId());
//			
//			System.out.println("findMember.getId() ==== " + findMember.getId());
//			System.out.println("findMember.getName() ===== " + findMember.getName());
//			System.out.println("findMember.getTeam().getName() ==== " + findMember.getTeam().getName());
//			System.out.println("findMember.getLocker().getName() ==== " + findMember.getLocker().getName());
			
			List<Member> members = em.createQuery("select m from Member m", Member.class)
									.getResultList();
			
			Parent parent = new Parent();
			
			Child child1 = new Child();
			Child child2 = new Child();
			
			parent.addChild(child1);
			parent.addChild(child2);
			
			em.persist(parent);
			
			em.flush();
			em.clear();
			
			Parent findParent = em.find(Parent.class, parent.getId());
			
			System.out.println("findParent.getChilds().size() ===== " + findParent.getChilds().size());
			findParent.getChilds().remove(0);
			System.out.println("findParent.getChilds().size() ===== " + findParent.getChilds().size());
			
			et.commit();
			
		} catch (Exception e) {
			
			et.rollback();
			e.printStackTrace();
			
		} finally {
			
			em.close();
			emf.close();
			
		}
		
	}
	
}