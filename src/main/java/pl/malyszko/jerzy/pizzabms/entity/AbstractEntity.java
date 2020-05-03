package pl.malyszko.jerzy.pizzabms.entity;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * @author Jerzy Mayszko
 *
 */
@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Long id;

	@Version
	private Long version;

	@Override
	public boolean equals(Object obj) {
		if (!Objects.equals(this.getClass(), obj.getClass()))
			return false;
		return Objects.equals(this.id, ((AbstractEntity) obj).id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
