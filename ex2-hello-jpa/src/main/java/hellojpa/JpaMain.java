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
			
			Team team = new Team();
			team.setName("Team_A");
			team.setInUser("ADMIN");
			team.setInDate(LocalDateTime.now());
			
			em.persist(team);
			
			Locker locker = new Locker();
			locker.setName("locker 1");
			
			Locker locker2 = new Locker();
			locker2.setName("locker 2");
			
			em.persist(locker);
			em.persist(locker2);
			
			Player player = new Player();
			
			player.setName("KIM");
			// 1차 캐시에 저장된 team에서 ID를 꺼내 set
//			player.setTeamId(team.getId()); 
			player.changeTeam(team);
			player.setLocker(locker);
			player.setInUser("KIM");
			player.setInDate(LocalDateTime.now());
			
			Player player2 = new Player();
			player2.setName("HAN");
			player2.changeTeam(team);
			player2.setLocker(locker2);
			player2.setInUser("HAN");
			player2.setInDate(LocalDateTime.now());
			
			em.persist(player);
			em.persist(player2);
			
//			em.flush();
//			em.clear();
			
			// 현재는 연관관계 매핑이 돼 있지 않기 때문에 Player를 먼저 조회 후 
			// 조회된 Player의 TeamId를 통해 Team 객체 조회 
			// 객체지향적이지 못하다. 
			Player getPlayer = em.find(Player.class, player.getId());
//			Team getTeam = em.find(Team.class, getPlayer.getTeamId());
//			Team getTeam = getPlayer.getTeam();
			List<Player> getPlayers = getPlayer.getTeam().getPlayers();
			
			for ( Player p : getPlayers ) 
				System.out.println(p.getName());
			
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
			
//			Movie findMovie = em.find(Movie.class, movie.getId());
			
//			System.out.println("findMovie.getName() ===== " + findMovie.getName());
			
			Item findItem = em.find(Item.class, movie.getId());
			
			System.out.println("findItem.getName() ==== " + findItem.getName());
			
			et.commit();
			
		} catch (Exception e) {
			
			et.rollback();
			
		} finally {
			
			em.close();
			emf.close();
			
		}
		
	}
	
}