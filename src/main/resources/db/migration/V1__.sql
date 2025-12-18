CREATE TABLE food
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    price         DECIMAL      NOT NULL,
    available     BIT(1)       NOT NULL,
    CONSTRAINT pk_food PRIMARY KEY (id)
);

CREATE TABLE `order`
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT NOT NULL,
    food_id    BIGINT NOT NULL,
    status     VARCHAR(255) NULL,
    created_at datetime NULL,
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE user
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    telegram_user_id BIGINT NOT NULL,
    username         VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_telegram_user UNIQUE (telegram_user_id);

ALTER TABLE `order`
    ADD CONSTRAINT FK_ORDER_ON_FOOD FOREIGN KEY (food_id) REFERENCES food (id);

ALTER TABLE `order`
    ADD CONSTRAINT FK_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);