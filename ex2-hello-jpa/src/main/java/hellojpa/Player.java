package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

// 연관 관계 매핑 
@Entity
public class Player {

	@Id
	@GeneratedValue
	@Column(name = "PLAYER_ID")
	private Long id;
	
	@Column(name = "PLAYER_NAME")
	private String name;
	
//	@Column(name = "TEAM_ID")
//	private Long teamId;
	
	@ManyToOne // N(Player) : 1(Team)
	@JoinColumn(name = "TEAM_ID") // join 컬럼을 명시 
	private Team team;

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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
