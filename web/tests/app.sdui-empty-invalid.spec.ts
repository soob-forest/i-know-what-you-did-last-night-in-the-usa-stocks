import { test, expect } from '@playwright/test';

test.describe('Dashboard /app (SDUI - 빈/미지원)', () => {
  test('빈 상태 처리', async ({ page }) => {
    await page.goto('/app?sdui=1&range=empty');
    await expect(page.getByText('표시할 뉴스가 없습니다')).toBeVisible();
  });

  test('알 수 없는 블록은 무시', async ({ page }) => {
    await page.goto('/__test__/ui/app?range=invalid');
    const res = await page.request.get('/__test__/ui/app?range=invalid');
    expect(res.ok()).toBeTruthy();
    // 렌더 경로에서는 UnknownBlock을 건너뛰도록 구현되어 있으므로, /app 경로에선 영향 없음
    await page.goto('/app?sdui=1&range=invalid');
    // 최소한 툴바는 보임
    await expect(page.getByRole('link', { name: 'Overnight' })).toBeVisible();
  });
});

