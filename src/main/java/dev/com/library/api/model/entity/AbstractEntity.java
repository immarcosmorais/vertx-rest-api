package dev.com.library.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractEntity {

  public static final String ID = "_id";
  public static final String CREATED_DATE = "created_date";
  public static final String LAST_MODIFIED_DATE = "last_modified_date";

  @JsonProperty(ID)
  private String id;
  @JsonProperty(CREATED_DATE)
  private Date createdDate;
  @JsonProperty(LAST_MODIFIED_DATE)
  private Date lastModifiedDate;

}
