-- INSERT INTO user_profile (id, email, games_amount, name, points_amount, surname) SELECT (1, 'eve@g.com', 0, 'Eve', 0, 'White')
--   FROM dual
--   WHERE NOT EXISTS(SELECT *
--                    FROM user_profile
--                    WHERE name = 'eve');
--
-- INSERT INTO auth_user (login, password, profile_id)
--   SELECT ('eve', 'password', 1)
--   FROM dual
--   WHERE NOT EXISTS(SELECT *
--                    FROM auth_user
--                    WHERE login = 'eve');
--
-- INSERT INTO user_profile (id, email, games_amount, name, points_amount, surname)
--   SELECT (2, 'tim@g.com', 0, 'Tim', 0, 'Black')
--   FROM dual
--   WHERE NOT EXISTS(SELECT *
--                    FROM user_profile
--                    WHERE name = 'tim');
--
-- INSERT INTO auth_user (login, password, profile_id)
--   SELECT ('tim', 'password', 1)
--   FROM dual
--   WHERE NOT EXISTS(SELECT *
--                    FROM auth_user
--                    WHERE login = 'tim');
--

INSERT INTO user_profile (id, email, game_amount, name, point_amount, surname, country)
VALUES (1, 'eve@g.com', 0, 'Eve', 0, 'White', 'Ukraine');

INSERT INTO auth_user (login, password, profile_id)
  VALUES ('eve', 'password', 1);

-- INSERT INTO user_profile (id, email, game_amount, name, point_amount, surname, avatar)
--   VALUES (2, 'tim@g.com', 0, 'Tim', 0, 'Black',
--    LOAD_FILE('C:\Users\Marina\img\ava_example.jpg'));

INSERT INTO user_profile (id, email, game_amount, name, point_amount, surname, country)
  VALUES (2, 'tim@g.com', 0, 'Tim', 0, 'Black', 'Russia');

INSERT INTO auth_user (login, password, profile_id)
  VALUES ('tim', 'password', 2);

INSERT INTO word (word)
 VALUES ('aquarium');

INSERT INTO word (word)
 VALUES ('mosquito');

INSERT INTO word (word)
 VALUES ('shark');

INSERT INTO word (word)
 VALUES ('bridge');

INSERT INTO word (word)
 VALUES ('monitor');

INSERT INTO word (word)
 VALUES ('athlete');

INSERT INTO word (word)
 VALUES ('garlic');

INSERT INTO word (word)
 VALUES ('ostrich');

INSERT INTO word (word)
 VALUES ('engine');

INSERT INTO word (word)
 VALUES ('downtown');

INSERT INTO word (word)
 VALUES ('plasticine');

INSERT INTO word (word)
 VALUES ('distance');

INSERT INTO word (word)
 VALUES ('wife');

INSERT INTO word (word)
 VALUES ('husband');

INSERT INTO word (word)
 VALUES ('walrus');

INSERT INTO word (word)
 VALUES ('laziness');

INSERT INTO word (word)
 VALUES ('pirate');

INSERT INTO word (word)
 VALUES ('smoke');

INSERT INTO word (word)
 VALUES ('birch');

INSERT INTO word (word)
 VALUES ('bite');

INSERT INTO word (word)
 VALUES ('employment');

INSERT INTO word (word)
 VALUES ('island');

INSERT INTO word (word)
 VALUES ('nightingale');

INSERT INTO word (word)
 VALUES ('donor');

INSERT INTO word (word)
 VALUES ('radiation');

INSERT INTO word (word)
 VALUES ('blonde');

INSERT INTO word (word)
 VALUES ('slave');

INSERT INTO word (word)
 VALUES ('celebrity');

INSERT INTO word (word)
 VALUES ('escalator');

INSERT INTO word (word)
 VALUES ('grove');

INSERT INTO word (word)
 VALUES ('mammoth');

INSERT INTO word (word)
 VALUES ('aluminium');

INSERT INTO word (word)
 VALUES ('duke');

INSERT INTO word (word)
 VALUES ('leopard');

INSERT INTO word (word)
 VALUES ('spring');

INSERT INTO word (word)
 VALUES ('voilet');

INSERT INTO word (word)
 VALUES ('pet');

INSERT INTO word (word)
 VALUES ('cossak');

INSERT INTO word (word)
 VALUES ('cheesecake');

INSERT INTO word (word)
 VALUES ('pensioner');

INSERT INTO word (word)
 VALUES ('millionaire');

INSERT INTO word (word)
 VALUES ('buffoon');

INSERT INTO word (word)
 VALUES ('pyramid');

INSERT INTO word (word)
 VALUES ('tango');

INSERT INTO word (word)
 VALUES ('flattery');

INSERT INTO word (word)
 VALUES ('musketeer');

INSERT INTO word (word)
 VALUES ('mosquito');

INSERT INTO word (word)
 VALUES ('baseball');

INSERT INTO word (word)
 VALUES ('football');

INSERT INTO word (word)
 VALUES ('basketball');

INSERT INTO word (word)
 VALUES ('biathlon');

INSERT INTO word (word)
 VALUES ('space');

INSERT INTO word (word)
 VALUES ('ballad');

INSERT INTO word (word)
 VALUES ('wolverine');

INSERT INTO word (word)
 VALUES ('intrigue');

INSERT INTO word (word)
 VALUES ('proposal');

INSERT INTO word (word)
 VALUES ('lilac');

INSERT INTO word (word)
 VALUES ('brooch');

INSERT INTO word (word)
 VALUES ('paste');

INSERT INTO word (word)
 VALUES ('grapes');

INSERT INTO word (word)
 VALUES ('kimono');

INSERT INTO word (word)
 VALUES ('service');

INSERT INTO word (word)
 VALUES ('cougar');

INSERT INTO word (word)
 VALUES ('liver');

INSERT INTO word (word)
 VALUES ('interest');

INSERT INTO word (word)
 VALUES ('corrosion');

INSERT INTO word (word)
 VALUES ('hang-glider');

INSERT INTO word (word)
 VALUES ('migraine');

INSERT INTO word (word)
 VALUES ('matter');

INSERT INTO word (word)
 VALUES ('ballast');

INSERT INTO word (word)
 VALUES ('festival');

INSERT INTO word (word)
 VALUES ('plunger');

INSERT INTO word (word)
 VALUES ('excursion');

INSERT INTO word (word)
 VALUES ('caviar');

INSERT INTO word (word)
 VALUES ('gladiator');

INSERT INTO word (word)
 VALUES ('mainland');

INSERT INTO word (word)
 VALUES ('climate');

INSERT INTO word (word)
 VALUES ('radiator');

INSERT INTO word (word)
 VALUES ('stomach');

INSERT INTO word (word)
 VALUES ('history');

INSERT INTO word (word)
 VALUES ('constellation');

INSERT INTO word (word)
 VALUES ('news');

INSERT INTO word (word)
 VALUES ('origami');

INSERT INTO word (word)
 VALUES ('peninsula');

INSERT INTO word (word)
 VALUES ('makeup');

INSERT INTO word (word)
 VALUES ('cheetah');

INSERT INTO word (word)
 VALUES ('friendship');

INSERT INTO word (word)
 VALUES ('icon');

INSERT INTO word (word)
 VALUES ('bijouterie');

INSERT INTO word (word)
 VALUES ('race');

INSERT INTO word (word)
 VALUES ('weight');

INSERT INTO word (word)
 VALUES ('swan');

INSERT INTO word (word)
 VALUES ('exchange');

INSERT INTO word (word)
 VALUES ('advertising');

INSERT INTO word (word)
 VALUES ('myth');

INSERT INTO word (word)
 VALUES ('faith');

INSERT INTO word (word)
 VALUES ('indigo');

INSERT INTO word (word)
 VALUES ('comment');

INSERT INTO word (word)
 VALUES ('poem');

INSERT INTO word (word)
 VALUES ('panda');

INSERT INTO word (word)
 VALUES ('soul');

INSERT INTO word (word)
 VALUES ('kangaroo');

INSERT INTO word (word)
 VALUES ('army');

INSERT INTO word (word)
 VALUES ('abyss');

INSERT INTO word (word)
 VALUES ('mafia');

INSERT INTO word (word)
 VALUES ('sphinx');

INSERT INTO word (word)
 VALUES ('period');

INSERT INTO word (word)
 VALUES ('journey');

INSERT INTO word (word)
 VALUES ('time');

INSERT INTO word (word)
 VALUES ('exhibition');

INSERT INTO word (word)
 VALUES ('pomegranate');

INSERT INTO word (word)
 VALUES ('aroma');

INSERT INTO word (word)
 VALUES ('lemur');

INSERT INTO word (word)
 VALUES ('baguette');

INSERT INTO word (word)
 VALUES ('hug');

INSERT INTO word (word)
 VALUES ('initiative');

INSERT INTO word (word)
 VALUES ('zephyr');

INSERT INTO word (word)
 VALUES ('excavator');

INSERT INTO word (word)
 VALUES ('confidence');

INSERT INTO word (word)
 VALUES ('ruler');

INSERT INTO word (word)
 VALUES ('sunset');

INSERT INTO word (word)
 VALUES ('feeling');

INSERT INTO word (word)
 VALUES ('sign');

INSERT INTO word (word)
 VALUES ('parachute');

INSERT INTO word (word)
 VALUES ('fortune');

INSERT INTO word (word)
 VALUES ('holiday');

INSERT INTO word (word)
 VALUES ('search');

INSERT INTO word (word)
 VALUES ('music');

INSERT INTO word (word)
 VALUES ('wire');

INSERT INTO word (word)
 VALUES ('napkin');

INSERT INTO word (word)
 VALUES ('burn');

INSERT INTO word (word)
 VALUES ('anger');

INSERT INTO word (word)
 VALUES ('handshake');

INSERT INTO word (word)
 VALUES ('beauty');

INSERT INTO word (word)
 VALUES ('snowfall');

INSERT INTO word (word)
 VALUES ('youth');

INSERT INTO word (word)
 VALUES ('peacock');

INSERT INTO word (word)
 VALUES ('article');

INSERT INTO word (word)
 VALUES ('fact');

INSERT INTO word (word)
 VALUES ('fortress');

INSERT INTO word (word)
 VALUES ('knight');

INSERT INTO word (word)
 VALUES ('pavement');

INSERT INTO word (word)
 VALUES ('pedestrian');

INSERT INTO word (word)
 VALUES ('traffic');

INSERT INTO word (word)
 VALUES ('cook');

INSERT INTO word (word)
 VALUES ('applause');

INSERT INTO word (word)
 VALUES ('lesson');

INSERT INTO word (word)
 VALUES ('story');

INSERT INTO word (word)
 VALUES ('criticism');

INSERT INTO word (word)
 VALUES ('barrier');

INSERT INTO word (word)
 VALUES ('dinosaur');

INSERT INTO word (word)
 VALUES ('compasses');

INSERT INTO word (word)
 VALUES ('compass');

INSERT INTO word (word)
 VALUES ('cocoa');

INSERT INTO word (word)
 VALUES ('melody');

INSERT INTO word (word)
 VALUES ('transmission');

INSERT INTO word (word)
 VALUES ('neon');

INSERT INTO word (word)
 VALUES ('border');

INSERT INTO word (word)
 VALUES ('picture');

INSERT INTO word (word)
 VALUES ('fan');

INSERT INTO word (word)
 VALUES ('currency');

INSERT INTO word (word)
 VALUES ('announcement');

INSERT INTO word (word)
 VALUES ('fashion');

INSERT INTO word (word)
 VALUES ('program');

INSERT INTO word (word)
 VALUES ('hate');

INSERT INTO word (word)
 VALUES ('rhyme');

INSERT INTO word (word)
 VALUES ('blindness');

INSERT INTO word (word)
 VALUES ('deafness');

INSERT INTO word (word)
 VALUES ('muteness');

INSERT INTO word (word)
 VALUES ('samurai');

INSERT INTO word (word)
 VALUES ('shame');