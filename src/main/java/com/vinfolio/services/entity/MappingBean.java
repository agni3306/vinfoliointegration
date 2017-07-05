package com.vinfolio.services.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Mapping")
public class MappingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MappingBean() {
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_pk")
	private Long mappingPk;

	@Column(name = "sourcefield")
	private String sourceField;
	@Column(name = "targetfield")
	private String targetField;

	@Column(name = "integration_fk")
	private String integrationFk;
	@Column(name = "transformation")
	private String transformation;
	@Column(name = "validation")
	private String validation;
	
	@Column(name = "defaultvalue")
	private String defaultValue;
	

	public Long getMappingPk() {
		return mappingPk;
	}

	public void setMappingPk(Long mappingPk) {
		this.mappingPk = mappingPk;
	}

	public String getSourceField() {
		return sourceField;
	}

	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}

	public String getTargetField() {
		return targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getIntegrationFk() {
		return integrationFk;
	}

	public void setIntegration_fk(String integrationFk) {
		this.integrationFk = integrationFk;
	}

	public String getTransformation() {
		return transformation;
	}

	public void setTransformation(String transformation) {
		this.transformation = transformation;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}