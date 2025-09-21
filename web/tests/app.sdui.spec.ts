import { test, expect } from '@playwright/test';

test.describe('Dashboard /app (SDUI)', () => {
  test('스키마 렌더: 카드와 링크 표시(overnight)', async ({ page }) => {
    await page.goto('/app?sdui=1&range=overnight');
    await expect(page.getByText('Apple Inc. (AAPL)')).toBeVisible();
    await expect(page.getByRole('link', { name: /Launch article/ })).toBeVisible();
  });

  test('범위 토글: today 전환', async ({ page }) => {
    await page.goto('/app?sdui=1&range=overnight');
    await page.getByRole('link', { name: 'Today' }).click();
    await expect(page.getByText('NVIDIA (NVDA)')).toBeVisible();
    await expect(page.getByText('Apple Inc. (AAPL)')).toHaveCount(0);
  });
});

