
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `user` ADD PRIMARY KEY (`id`);

INSERT INTO `user` (`id`, `email`, `enabled`, `firstname`, `lastname`, `password`, `role`) VALUES
(1, 'gvilledieu@laposte.net', b'1', 'Guillaume', 'VILLEDIEU', '$2a$10$.z5oMlGLLaMFR7Piv7Pch.GVoyWu8mQ8E.TUIRJ3oXQphLCzZPhFW', 'SUPERADMIN'),
(2, 'afouquet@yellownetwork.fr', b'1', 'Aurélien', 'Fouquet', '$2a$10$ZARM5TOukwCibjZkTbLN.eYaCi9Ri8i0hikvJ/kcYkdPUmuh9kZnK', 'SUPERADMIN');
