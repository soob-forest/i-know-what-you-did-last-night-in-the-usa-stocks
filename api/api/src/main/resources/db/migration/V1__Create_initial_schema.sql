-- V1__Create_initial_schema.sql
-- 도메인 분석 결과:
-- 1. Member (회원) - userId, subscription과 1:1 관계
-- 2. Subscription (구독) - pushAt 시간, member와 1:1, subscription_stock과 1:N
-- 3. Stock (주식) - name, ticker, subscription_stock과 1:N, news와 1:N
-- 4. SubscriptionStock (구독-주식 연결) - member의 특정 주식 구독을 관리
-- 5. News (뉴스) - date, summary, stock과 N:1, news_link와 1:N
-- 6. NewsLink (뉴스 링크) - title, url, source, news와 N:1

-- Member 테이블
CREATE TABLE member (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    subscription_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Subscription 테이블
CREATE TABLE subscription (
    id BIGSERIAL PRIMARY KEY,
    push_at TIME,
    member_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Stock 테이블
CREATE TABLE stock (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    ticker VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SubscriptionStock 테이블 (Many-to-Many 연결 테이블)
CREATE TABLE subscription_stock (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL,
    stock_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(subscription_id, stock_id)
);

-- News 테이블
CREATE TABLE news (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    summary TEXT,
    stock_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- NewsLink 테이블
CREATE TABLE news_link (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500),
    url TEXT NOT NULL,
    source VARCHAR(255),
    news_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Foreign Key 제약조건 추가
ALTER TABLE member ADD CONSTRAINT fk_member_subscription
    FOREIGN KEY (subscription_id) REFERENCES subscription(id);

ALTER TABLE subscription ADD CONSTRAINT fk_subscription_member
    FOREIGN KEY (member_id) REFERENCES member(id);

ALTER TABLE subscription_stock ADD CONSTRAINT fk_subscription_stock_subscription
    FOREIGN KEY (subscription_id) REFERENCES subscription(id) ON DELETE CASCADE;

ALTER TABLE subscription_stock ADD CONSTRAINT fk_subscription_stock_stock
    FOREIGN KEY (stock_id) REFERENCES stock(id) ON DELETE CASCADE;

ALTER TABLE news ADD CONSTRAINT fk_news_stock
    FOREIGN KEY (stock_id) REFERENCES stock(id) ON DELETE CASCADE;

ALTER TABLE news_link ADD CONSTRAINT fk_news_link_news
    FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE;

-- 인덱스 추가 (성능 최적화)
CREATE INDEX idx_member_user_id ON member(user_id);
CREATE INDEX idx_stock_ticker ON stock(ticker);
CREATE INDEX idx_news_date ON news(date);
CREATE INDEX idx_news_stock_date ON news(stock_id, date);
CREATE INDEX idx_subscription_stock_subscription ON subscription_stock(subscription_id);
CREATE INDEX idx_subscription_stock_stock ON subscription_stock(stock_id);
CREATE INDEX idx_news_link_news ON news_link(news_id);