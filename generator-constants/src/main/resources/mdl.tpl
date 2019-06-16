package #packageName#;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * #tableComments#
 * @author liuzhsh
 */
@Entity
@Table(name = "#tableName#")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class #mdlName# implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

#allFields#

	// Constructor

	/** default constructor */
	public #mdlName#() {
	}
#constructorHasKey#
	// Property accessors

#gettersAndSetters#
	// ext. field

}
