
CREATE TABLE IF NOT EXISTS trader (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    wallet_balance DOUBLE,
    eth_balance DOUBLE,
    btc_balance DOUBLE
);
INSERT INTO trader (username, wallet_balance,eth_balance,btc_balance) VALUES ('trader1', 50000.0,0,0);
