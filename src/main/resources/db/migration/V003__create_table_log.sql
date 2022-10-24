
CREATE TABLE `log` (
   `id` bigint(20) NOT NULL,
   `date` datetime NOT NULL,
   `user_id` bigint(20) NOT NULL,
   `action` varchar(255) DEFAULT NULL,
   `comment` text DEFAULT NULL,
   `public_ip` VARCHAR(255) DEFAULT NULL,
   `user_agent` VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `log` ADD PRIMARY KEY (`id`);
ALTER TABLE `log` ADD CONSTRAINT FK_LOG_user_id_to_USER_id FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);