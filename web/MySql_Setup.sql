-- login with root to create user, DB and table and provide grants
   
create database test_servlets;
 
use test_servlets;
 
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(40) UNIQUE NOT NULL DEFAULT '',
  `first_name` varchar(40) DEFAULT 'USA',
  `last_name` varchar(40) DEFAULT 'USA',
  `password` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
