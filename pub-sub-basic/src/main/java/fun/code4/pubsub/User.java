package fun.code4.pubsub;

import java.time.Instant;

public class User {

  private String name;
  private Instant timestamp;
  private Gender gender;

  private User() {
  }

  public User(String name, Gender gender) {
    this.name = name;
    this.gender = gender;
    this.timestamp = Instant.now();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  @Override
  public String toString() {
    return "User{" +
        "name='" + name + '\'' +
        ", timestamp=" + timestamp +
        ", gender=" + gender +
        '}';
  }
}
