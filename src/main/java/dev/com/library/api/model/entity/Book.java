package dev.com.library.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book extends AbstractEntity {

  public static final String TITLE = "title";
  public static final String AUTHOR = "author";
  public static final String YEAR = "year";
  public static final String PAGES = "pages";
  public static final String ISBN = "isbn";
  public static final String LANGUAGE = "language";
  public static final String PUBLISHER = "publisher";
  public static final String COUNTRY = "country";
  public static final String IMAGE_LINK = "image_link";

  private String title;
  private String author;
  private Integer year;
  private Integer pages;
  private String isbn;
  private String language;
  private String publisher;
  private String country;
  @JsonProperty(IMAGE_LINK)
  private String imageLink;


}
