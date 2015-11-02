INSERT INTO user_profile (id, email, game_amount, name, point_amount, surname, country)
VALUES (1, 'eve@g.com', 0, 'Eve', 0, 'White', 'Ukraine');

INSERT INTO auth_user (login, password, profile_id)
  VALUES ('eve', 'password', 1);

INSERT INTO user_profile (id, email, game_amount, name, point_amount, surname, country)
  VALUES (2, 'tim@g.com', 0, 'Tim', 0, 'Black', 'Russia');

INSERT INTO auth_user (login, password, profile_id)
  VALUES ('tim', 'password', 2);

INSERT INTO word (word)
 VALUES ('aquarium');

INSERT INTO word (word)
 VALUES ('mosquito');