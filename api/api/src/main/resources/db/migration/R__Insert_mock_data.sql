-- R__Insert_mock_data.sql
-- 테스트 및 개발용 Mock 데이터 삽입

-- 기존 데이터 삭제 (반복 실행을 위해)
-- 외래키 제약조건을 고려한 순서로 삭제
DELETE FROM news_link;
DELETE FROM news;
DELETE FROM subscription_stock;
UPDATE member SET subscription_id = NULL;
DELETE FROM subscription;
DELETE FROM member;
DELETE FROM stock;

-- Auto-increment ID 리셋
ALTER SEQUENCE member_id_seq RESTART WITH 1;
ALTER SEQUENCE subscription_id_seq RESTART WITH 1;
ALTER SEQUENCE stock_id_seq RESTART WITH 1;
ALTER SEQUENCE subscription_stock_id_seq RESTART WITH 1;
ALTER SEQUENCE news_id_seq RESTART WITH 1;
ALTER SEQUENCE news_link_id_seq RESTART WITH 1;

-- 1. Stock 데이터 (주요 미국 주식들)
INSERT INTO stock (name, ticker, created_at, modified_at) VALUES
('Apple Inc.', 'AAPL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Microsoft Corporation', 'MSFT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Amazon.com Inc.', 'AMZN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Alphabet Inc. Class A', 'GOOGL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NVIDIA Corporation', 'NVDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tesla Inc.', 'TSLA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Meta Platforms Inc.', 'META', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Berkshire Hathaway Inc. Class B', 'BRK.B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('UnitedHealth Group Incorporated', 'UNH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Johnson & Johnson', 'JNJ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('JPMorgan Chase & Co.', 'JPM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Visa Inc. Class A', 'V', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Procter & Gamble Company', 'PG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mastercard Incorporated Class A', 'MA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Home Depot Inc.', 'HD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. Member 데이터 (테스트 사용자들)
INSERT INTO member (user_id, created_at, modified_at) VALUES
('test_user_001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test_user_002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test_user_003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('slack_user_john', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('slack_user_jane', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. Subscription 데이터 (각 사용자별 구독 설정) - member_id 없이 먼저 생성
INSERT INTO subscription (push_at, created_at, modified_at) VALUES
('09:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('10:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('15:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('08:30:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('16:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Member와 Subscription 연결 업데이트
UPDATE member SET subscription_id = 1 WHERE id = 1;
UPDATE member SET subscription_id = 2 WHERE id = 2;
UPDATE member SET subscription_id = 3 WHERE id = 3;
UPDATE member SET subscription_id = 4 WHERE id = 4;
UPDATE member SET subscription_id = 5 WHERE id = 5;

-- 4. SubscriptionStock 데이터 (사용자별 주식 구독)
INSERT INTO subscription_stock (subscription_id, stock_id, created_at, modified_at) VALUES
-- test_user_001: 테크 주식 중심
(1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- AAPL
(1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- MSFT
(1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- NVDA
(1, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- TSLA

-- test_user_002: 대형주 포트폴리오
(2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- AAPL
(2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- AMZN
(2, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- GOOGL
(2, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- BRK.B

-- test_user_003: 금융/소비재 중심
(3, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- JPM
(3, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- V
(3, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- PG
(3, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- HD

-- slack_user_john: 다양한 섹터
(4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- AAPL
(4, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- TSLA
(4, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- UNH
(4, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- JNJ

-- slack_user_jane: 성장주 중심
(5, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- MSFT
(5, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- NVDA
(5, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- META
(5, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- MA

-- 5. News 데이터 (최근 뉴스 샘플)
INSERT INTO news (date, summary, stock_id, created_at, modified_at) VALUES
-- Apple 관련 뉴스
(CURRENT_DATE, 'Apple announces record Q4 earnings driven by strong iPhone 15 sales and services growth. The company reported revenue of $119.5 billion, beating analyst expectations.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(CURRENT_DATE - INTERVAL '1 day', 'Apple unveils new AI features for iOS 18, focusing on enhanced Siri capabilities and machine learning integration across all apps.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Microsoft 관련 뉴스
(CURRENT_DATE, 'Microsoft Azure revenue grows 29% year-over-year as cloud adoption accelerates. AI services contribute significantly to growth.', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(CURRENT_DATE - INTERVAL '2 days', 'Microsoft announces partnership with OpenAI for next-generation productivity tools integration in Office 365.', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Tesla 관련 뉴스
(CURRENT_DATE, 'Tesla delivers 435,000 vehicles in Q4, exceeding guidance despite supply chain challenges. Model Y remains best-selling EV globally.', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(CURRENT_DATE - INTERVAL '1 day', 'Tesla Supercharger network opens to all EV brands in North America, potentially creating new revenue stream.', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- NVIDIA 관련 뉴스
(CURRENT_DATE, 'NVIDIA data center revenue reaches $47.5 billion in fiscal 2024, driven by unprecedented AI chip demand from major tech companies.', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(CURRENT_DATE - INTERVAL '3 days', 'NVIDIA announces next-generation Blackwell architecture for AI training, promising 5x performance improvement over current generation.', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 6. NewsLink 데이터 (뉴스 출처 링크들)
INSERT INTO news_link (title, url, source, news_id, created_at, modified_at) VALUES
-- Apple Q4 earnings 관련 링크들
('Apple Reports Record Fourth Quarter Results', 'https://www.apple.com/newsroom/2024/11/apple-reports-fourth-quarter-results/', 'Apple Newsroom', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Apple Beats Q4 Earnings Expectations on Strong iPhone Sales', 'https://www.cnbc.com/2024/11/01/apple-earnings-q4-2024.html', 'CNBC', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Apple Stock Rises After Earnings Beat', 'https://www.wsj.com/articles/apple-stock-earnings-beat-2024', 'Wall Street Journal', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Apple AI features 관련 링크들
('Apple Intelligence: The AI Features Coming to iOS 18', 'https://www.apple.com/newsroom/2024/10/apple-intelligence-ios-18/', 'Apple Newsroom', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('How Apple Intelligence Will Change Your iPhone Experience', 'https://www.techcrunch.com/2024/10/apple-intelligence-ios-18', 'TechCrunch', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Microsoft Azure 관련 링크들
('Microsoft Azure Delivers Strong Q4 Growth', 'https://news.microsoft.com/2024/11/microsoft-azure-q4-results/', 'Microsoft News', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Microsoft Cloud Revenue Surges on AI Demand', 'https://www.reuters.com/technology/microsoft-cloud-revenue-ai-2024-11-01', 'Reuters', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Microsoft OpenAI partnership 관련 링크들
('Microsoft and OpenAI Expand Partnership for Enterprise AI', 'https://news.microsoft.com/2024/10/microsoft-openai-partnership-expansion/', 'Microsoft News', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Microsoft Integrates Advanced AI into Office 365', 'https://www.bloomberg.com/news/microsoft-office-ai-integration-2024', 'Bloomberg', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Tesla Q4 deliveries 관련 링크들
('Tesla Delivers Record 435K Vehicles in Q4 2024', 'https://ir.tesla.com/news-releases/news-release-details/tesla-vehicle-deliveries-q4-2024', 'Tesla Investor Relations', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tesla Beats Delivery Expectations Despite Challenges', 'https://www.cnbc.com/2024/11/02/tesla-q4-2024-deliveries.html', 'CNBC', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Tesla Supercharger 관련 링크들
('Tesla Opens Supercharger Network to All EVs', 'https://www.tesla.com/support/supercharger-network-expansion', 'Tesla Support', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tesla Supercharger Expansion Could Generate Billions', 'https://www.wsj.com/articles/tesla-supercharger-revenue-2024', 'Wall Street Journal', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- NVIDIA data center 관련 링크들
('NVIDIA Data Center Revenue Reaches Record $47.5B', 'https://nvidianews.nvidia.com/news/nvidia-data-center-revenue-fiscal-2024', 'NVIDIA Newsroom', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NVIDIA AI Chip Demand Drives Record Revenue', 'https://www.reuters.com/technology/nvidia-ai-chip-revenue-2024-11-01', 'Reuters', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- NVIDIA Blackwell 관련 링크들
('NVIDIA Unveils Blackwell: Next-Gen AI Architecture', 'https://nvidianews.nvidia.com/news/nvidia-blackwell-architecture-announcement', 'NVIDIA Newsroom', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NVIDIA Blackwell Promises 5x AI Performance Boost', 'https://www.techcrunch.com/2024/10/nvidia-blackwell-architecture', 'TechCrunch', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);