CREATE TABLE GroupeaseUser (
  id BIGSERIAL PRIMARY KEY,
  providerUserId TEXT NOT NULL UNIQUE,
  email TEXT NOT NULL,
  name TEXT NOT NULL,
  nickname TEXT NULL,
  pictureUrl TEXT NULL,
  lastUpdatedOn TIMESTAMP NOT NULL
);

CREATE TABLE Channel (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,
  description TEXT NULL,
  lastUpdatedOn TIMESTAMP NOT NULL
);

CREATE TABLE Member (
    id BIGSERIAL PRIMARY KEY,
    userId BIGSERIAL NOT NULL REFERENCES GroupeaseUser,
    ChannelId BIGSERIAL NOT NULL REFERENCES Channel,
    IsOwner BOOLEAN NOT NULL DEFAULT FALSE,
    -- Issue #31: Per channel profile fields to go here later
    skills TEXT NULL,
    availability TEXT NULL,
    goals TEXT NULL,
    LastUpdate TIMESTAMP NOT NULL
);

CREATE TABLE ChannelGroup (
    ID BIGSERIAL PRIMARY KEY,
    ChannelID BIGSERIAL NOT NULL REFERENCES Channel ON DELETE CASCADE,
    Name TEXT NOT NULL,
    Description TEXT,
    IsFull BOOLEAN NOT NULL DEFAULT FALSE,
    LastUpdate TIMESTAMP NOT NULL
);

CREATE TABLE GroupMember (
    GroupID BIGSERIAL NOT NULL REFERENCES ChannelGroup ON DELETE CASCADE,
    MemberID BIGSERIAL NOT NULL REFERENCES Member ON DELETE CASCADE,
    LastUpdate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (GroupID, MemberID)
);

CREATE TABLE ChannelInvitation (
    ID BIGSERIAL PRIMARY KEY,
    ChannelID BIGSERIAL NOT NULL REFERENCES Channel ON DELETE CASCADE,
    SenderID BIGSERIAL NOT NULL REFERENCES GroupeaseUser ON DELETE CASCADE,
    recipientId BIGSERIAL NOT NULL REFERENCES GroupeaseUser ON DELETE CASCADE,
    Comments TEXT,
    LastUpdate TIMESTAMP NOT NULL
);

CREATE TABLE ChannelJoinRequest (
    ID BIGSERIAL PRIMARY KEY,
    ChannelID BIGSERIAL NOT NULL REFERENCES Channel ON DELETE CASCADE,
    userId BIGSERIAL NOT NULL REFERENCES GroupeaseUser ON DELETE CASCADE,
    Comments TEXT,
    LastUpdate TIMESTAMP NOT NULL
);

CREATE TABLE GroupInvitation (
    ID BIGSERIAL PRIMARY KEY,
    GroupID BIGSERIAL NOT NULL REFERENCES ChannelGroup ON DELETE CASCADE,
    SenderID BIGSERIAL NOT NULL REFERENCES Member ON DELETE CASCADE,
    RecipientID BIGSERIAL NOT NULL REFERENCES Member ON DELETE CASCADE,
    Comments TEXT,
    LastUpdate TIMESTAMP NOT NULL
);

CREATE TABLE GroupJoinRequest (
    ID BIGSERIAL PRIMARY KEY,
    GroupID BIGSERIAL NOT NULL REFERENCES ChannelGroup ON DELETE CASCADE,
    SenderID BIGSERIAL NOT NULL REFERENCES GroupeaseUser ON DELETE CASCADE,
    Comments TEXT,
    LastUpdate TIMESTAMP NOT NULL
);

INSERT INTO Channel (
  name,
  description,
  lastUpdatedOn
) VALUES (
  'CS6675-Spring-2018',
  'Advanced Internet Applications - Spring 2018',
  CURRENT_TIMESTAMP
);

INSERT INTO Channel (
  name,
  description,
  lastUpdatedOn
) VALUES (
  'CS8803-BDS-Fall-2017',
  'Big Data Systems and Analytics - Fall 2017',
  CURRENT_TIMESTAMP
);

INSERT INTO GroupeaseUser (
    providerUserId, name, nickname, pictureUrl, Email, lastUpdatedOn
) VALUES (
    'phony-provider-1234567890', 'George P. Burdell', 'gpb01',
    'http://example.com/avatars/gatech/gpb01.png', 'gpburdell@gatech.edu', CURRENT_timestamp
) ;

INSERT INTO Member (
    ChannelID, UserID, IsOwner, skills, availability, goals, LastUpdate
) VALUES (
    (SELECT id FROM Channel WHERE Name='CS6675-Spring-2018'),
    (SELECT ID FROM GroupeaseUser WHERE nickname='gpb01'),
    TRUE,
    'Java, JavaScript, SQL',
    'Weekdays: 4pm - 10pm; Weekends: Any time',
    'Get an A and learn Angular',
    CURRENT_TIMESTAMP
);

INSERT INTO Member (
    ChannelID, UserID, IsOwner, skills, availability, goals, LastUpdate
) VALUES (
    (SELECT id FROM Channel WHERE Name='CS8803-BDS-Fall-2017'),
    (SELECT ID FROM GroupeaseUser WHERE nickname='gpb01'),
    TRUE,
    'Hadoop and Spark',
    'Mondays 7- 8pm',
    'Do just enough to pass the class',
    CURRENT_TIMESTAMP
);

INSERT INTO ChannelGroup (
    ChannelID, Name, Description, LastUpdate
) VALUES (
    (SELECT id FROM Channel WHERE Name='CS6675-Spring-2018'),
    'GroupEase', 'Create groups with ease', current_timestamp
);

INSERT INTO GroupMember (
    GroupID, MemberID, LastUpdate
) VALUES (
    (SELECT ID FROM ChannelGroup WHERE Name='GroupEase'),
    (SELECT Member.ID FROM Member
        INNER JOIN GroupeaseUser ON Member.UserID = GroupeaseUser.id
        INNER JOIN Channel ON Member.ChannelID = Channel.id
        WHERE GroupeaseUser.nickname='gpb01' AND Channel.name='CS6675-Spring-2018'),
    current_timestamp
);
