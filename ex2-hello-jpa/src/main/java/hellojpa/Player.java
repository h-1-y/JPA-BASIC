package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// 연관 관계 매핑 
@Entity
public class Player {

	@Id
	@GeneratedValue
	@Column(name = "PLAYER_ID")
	private Long id;
	
	@Column(name = "PLAYER_NAME")
	private String name;
	
	@Column(name = "TEAM_ID")
	private Long teamId;

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

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	
}
