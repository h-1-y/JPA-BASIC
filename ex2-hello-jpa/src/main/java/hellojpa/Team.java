package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team {

	@Id
	@GeneratedValue
	@Column(name = "TEAM_ID")
	private Long id;
	
	private String name;
	
	// 양방향 연관관계 세팅 
	@OneToMany(mappedBy = "team") // 1(Team) : N(Player) 
	// mappedBy - 어떤 객체와 연관 되어있는지 작성 ( 연관관계의 주인이 아닌경우 mappedBy 지정 / 주인이 아닌쪽은 읽기만 가능 )
	private List<Player> players = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
}
