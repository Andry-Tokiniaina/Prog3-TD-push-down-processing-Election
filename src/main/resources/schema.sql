CREATE TABLE candidate (
    id serial primary key,
    name text not null
);

CREATE TABLE voter (
    id serial primary key,
    name text not null
);

CREATE TYPE vote_type AS ENUM('VALID', 'BLANK', 'NULL');

CREATE TABLE vote (
  id SERIAL PRIMARY KEY,
  candidate_id INT REFERENCES candidate(id),
  voter_id INT NOT NULL REFERENCES voter(id),
  vote_type vote_type NOT NULL);