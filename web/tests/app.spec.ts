import { test, expect } from '@playwright/test';

test.describe('Dashboard /app', () => {
  test('기본 렌더: 카드와 링크 표시(overnight)', async ({ page }) => {
    await page.goto('/app?range=overnight');
    await expect(page.getByText('Apple Inc. (AAPL)')).toBeVisible();
    await expect(page.getByText('Tesla (TSLA)')).toBeVisible();
    await expect(page.getByRole('link', { name: /Launch article/ })).toBeVisible();
  });

  test('범위 토글: overnight -> today', async ({ page }) => {
    await page.goto('/app?range=overnight');
    await page.getByRole('link', { name: 'Today' }).click();
    await expect(page.getByText('NVIDIA (NVDA)')).toBeVisible();
    await expect(page.getByText('Apple Inc. (AAPL)')).toHaveCount(0);
  });

  test('빈 상태: empty', async ({ page }) => {
    await page.goto('/app?range=empty');
    await expect(page.getByText('표시할 뉴스가 없습니다')).toBeVisible();
  });

  test('오류 처리: 500 응답 시 빈 상태로 폴백', async ({ page }) => {
    await page.goto('/app?range=error');
    await expect(page.getByText('표시할 뉴스가 없습니다')).toBeVisible();
  });
});

