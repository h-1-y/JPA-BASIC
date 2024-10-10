package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class fetchMain {

	public static void main(String[] args) {
	
		// EntityManagerFactory는 하나만 생성해서 애플리케이션 전체에서 공유 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		// EntityManager는 스레드간 공유 X(사용하고 버려야 함)
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		et.begin();
		
		try {
			
			// fetch join 실무에서 중요함 !!!!
			
			Team teamA = new Team();
			teamA.setName("Team A");
			
			em.persist(teamA);
			
			Team teamB = new Team();
			teamB.setName("Team B");
			
			em.persist(teamB);
			
			Member member1 = new Member();
			
			member1.setUsername("member1");
			member1.setAge(20);
			member1.setTeam(teamA);
			member1.setType(MemberType.ADMIN);
			
			Member member2 = new Member();
			
			member2.setUsername("member2");
			member2.setAge(30);
			member2.setTeam(teamA);
			member2.setType(MemberType.USER);
			
			Member member3 = new Member();
			
			member3.setUsername("member3");
			member3.setAge(31);
			member3.setTeam(teamB);
			member3.setType(MemberType.USER);	
			
			em.persist(member1);
			em.persist(member2);
			em.persist(member3);
			
			em.flush();
			em.clear();
			
			String query = "select m from Member m";
			
			List<Member> members = em.createQuery(query, Member.class).getResultList();
			
			for ( Member m : members ) {
				System.out.println("m === " + m.getUsername() + " / " + m.getTeam().getName());
				// member1 <- TeamA ( SQL 쿼리 실행 )
				// member2 <- TeamA ( 위의 실행된 TeamA 쿼리문이 1차 캐시에 저장되므로 쿼리 안날아가고 1차캐시에서 매칭됨 )
				// member3 <- TeamB ( SQL 쿼리 실행 )
				// 쿼리가 총 3번 나감 member 쿼리 1번, TeamA 쿼리 1번, TeamB 쿼리 1번
				
				// member 100명 -> N + 1
			}
			
			em.flush();
			em.clear();
			
			
			// 해결책 -> fetch join 사용
			String fetchQuery = "select m from Member m join fetch m.team";
			
			List<Member> fetchMembers = em.createQuery(fetchQuery, Member.class).getResultList();
			
			for ( Member m : fetchMembers ) {
				System.out.println("m === " + m.getUsername() + " / " + m.getTeam().getName());
				// fetch join 사용시 join을 통해 한방 쿼리로 다 가져옴
			}
			
			em.flush();
			em.clear();
			
			
			// Collection fetch join 
			String collectionFetchQuery = "select t from Team t join fetch t.members";
			
			List<Team> teams = em.createQuery(collectionFetchQuery, Team.class).getResultList();
			
			for ( Team team : teams ) {
				System.out.println("team == " + team.getName() + " / " + team.getMembers().size());
				for ( Member member : team.getMembers() ) System.out.println("-- member == " + member);
			}
			
			em.flush();
			em.clear();
			
			
			// fetch join과 distinct 중복제거
			String distinctQuery = "select distinct t from Team t join fetch t.members";
			
			List<Team> distincTeams = em.createQuery(distinctQuery, Team.class).getResultList();
			
			for ( Team team : distincTeams ) {
				System.out.println("team == " + team.getName() + " / " + team.getMembers().size());
				for ( Member member : team.getMembers() ) System.out.println("-- member == " + member);
			}
			
			em.flush();
			em.clear();
			
			// fetch join의 특징과 한계
			// 페치 조인 대상에게 별칭(as)을 줄 수 없다. 
			// 둘 이상의 컬렉션은 페치 조인 할 수 없다.
			// 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다.
			// fetch join은 N + 1 문제를 해결하기 위해 사용
			
			// @BatchSize
			String sizeQuery = "select t from Team t";
			
			List<Team> batchTeams = em.createQuery(sizeQuery, Team.class).setFirstResult(0).setMaxResults(2).getResultList();
			
			for ( Team team : batchTeams ) {
				System.out.println("team == " + team.getName() + " / " + team.getMembers().size());
				for ( Member member : team.getMembers() ) System.out.println("-- member == " + member);
			}
			
			em.flush();
			em.clear();
			
			
			// Named Query
			// 미리 정의해서 이름을 부여해두고 사용하는 JPQL
			// 어노테이션, XML에 정의 
			// 어플리케이션 로딩 시점에 초기화 후 재사용
			// 애플리케이션 로딩 시점에 쿼리를 검증 
			List<Member> namedMember = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", "member1").getResultList();
			
			for ( Member m : namedMember ) System.out.println("m ==== " + m);
			
			em.flush();
			em.clear();
			
			// 벌크 연산
			// 쿼리 한번으로 여러 테이블 로우 변경
			int update = em.createQuery("update Member m set m.age = 20").executeUpdate();
			System.out.println("result count == " + update);
			
			Member findMember = em.find(Member.class, member3.getId());
			System.out.println(findMember.getAge());
			
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
