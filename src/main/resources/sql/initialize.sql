CREATE TYPE AUTHENTICATION_METHOD AS ENUM ('oauth1', 'oauth2', 'openId','userPassword');

CREATE TABLE USERS (
  ID BIGSERIAL PRIMARY KEY,
  USER_ID TEXT,
  FIRST_NAME TEXT,
  LAST_NAME TEXT,
  EMAIL TEXT,
  AVATAR_URL TEXT UNIQUE,
  AUTH_METHOD AUTHENTICATION_METHOD,
  -- Password
  HASHER TEXT,
  PASSWORD TEXT,
  SALT TEXT,
  -- OAUTH1 TYPE
  TOKEN TEXT,
  SECRET TEXT,
  -- OAUTH2 TYPE
  ACCESS_TOKEN TEXT,
  REFRSH_TOKEN TEXT,
  TOKEN_TYPE TEXT,
  EXPIRES_IN INTERVAL
);

CREATE TYPE REMOTE_SOURCE AS ENUM ('google', 'facebook', 'twitter');
CREATE TABLE REMOTE_USERS (
  USER_ID BIGSERIAL REFERENCES USERS(ID),
  REMOTE_USER_ID TEXT,
  SOURCE REMOTE_SOURCE,
  PRIMARY KEY (USER_ID, SOURCE)
);



CREATE TABLE ADDRESSES (
  ID BIGSERIAL PRIMARY KEY,
  LINE_1 TEXT,
  LINE_2 TEXT,
  LINE_3 TEXT,
  CITY TEXT,
  COUNTY_PROVICE TEXT,
  ZIP_OR_POSTCODE TEXT,
  COUNTRY TEXT,
  OTHER_ADDRESS_DETAILS TEXT
);


CREATE TYPE ADDRESS_TYPE AS ENUM ('residential', 'commercial');
CREATE TABLE USER_ADDRESSES (
  ID BIGSERIAL PRIMARY KEY,
  ADDRESS_ID BIGSERIAL REFERENCES ADDRESSES(ID),
  USER_ID BIGSERIAL REFERENCES USERS(ID),
  TYPE ADDRESS_TYPE 
);
