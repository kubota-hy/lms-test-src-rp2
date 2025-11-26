package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		//closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms");
		getEvidence(new Object() {},"test01");
		String pageTitle = WebDriverUtils.webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitle);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA02");
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		//DOM構築前に検索するとエラーになるため、表示されるまで待機する
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		WebElement courseDetail = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("ol.breadcrumb li.active")
						)
				);
		getEvidence(new Object() {},"test02");

		assertEquals("コース詳細", courseDetail.getText());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		// TODO ここに追加
		WebElement userDetailLink = webDriver.findElement(
				By.cssSelector("a[href='/lms/user/detail']"));

		userDetailLink.click();

		getEvidence(new Object() {},"test03");


		assertEquals("ユーザー詳細",webDriver.getTitle());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws InterruptedException {

		WebElement repairBtn = webDriver.findElement(
				By.xpath("//td[contains(text(),'2022年10月2日')]/following-sibling::td//input[@value='修正する']")
				);
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);",repairBtn);
		Thread.sleep(300);

		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", repairBtn);

		getEvidence(new Object() {},"test04");

		assertEquals("レポート登録 | LMS",webDriver.getTitle());


	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

		// 再描画前の要素を操作
		WebElement learningContent = webDriver.findElement(By.id("intFieldName_0"));
		learningContent.clear();

		WebElement submitBtn = webDriver.findElement(By.cssSelector("*[type='submit'].btn-primary"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);

		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitBtn);

		// 再描画後の新しいDOMを待つ（errorInputが付与される要素）
		wait.until(ExpectedConditions.attributeContains(By.id("intFieldName_0"), "class", "errorInput"));

		// スクショは描画後に実施
		getEvidence(new Object() {}, "test05");

		// 改めて新しいWebElementを取得
		WebElement degreeSelect = webDriver.findElement(By.id("intFieldName_0"));
		String className = degreeSelect.getAttribute("class");

		assertTrue(className.contains("errorInput"));
	}


	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() throws InterruptedException {
		// TODO ここに追加


		WebElement learningContent = webDriver.findElement(
				By.id("intFieldName_0"));

		learningContent.sendKeys("テストコード作成");

		//空白を選択
		Select select = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		select.selectByIndex(0); 


		WebElement submitBtn = webDriver.findElement(
				By.cssSelector("*[type='submit'].btn-primary"));

		//スクロールしてボタンを視界に入れる
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
		Thread.sleep(300);

		//強制クリック（物理クリックがブロックされるため）
		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", submitBtn);	

		getEvidence(new Object() {},"test06");

		WebElement degreeSelect = webDriver.findElement(By.id("intFieldValue_0"));
		String className = degreeSelect.getAttribute("class");

		assertTrue(className.contains("errorInput"));

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() throws InterruptedException {
		// TODO ここに追加
		WebElement achievementLevel = webDriver.findElement(
				By.id("content_0"));

		achievementLevel.clear();
		achievementLevel.sendKeys("零");


		WebElement submitBtn = webDriver.findElement(
				By.cssSelector("*[type='submit'].btn-primary"));

		//スクロールしてボタンを視界に入れる
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
		Thread.sleep(300);

		//強制クリック（物理クリックがブロックされるため）
		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", submitBtn);

		getEvidence(new Object() {},"test07");

		WebElement degreeSelect = webDriver.findElement(By.id("content_0"));
		String className = degreeSelect.getAttribute("class");

		assertTrue(className.contains("errorInput"));
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() throws InterruptedException {
		// TODO ここに追加
		WebElement achievementLevel = webDriver.findElement(
				By.id("content_0"));

		achievementLevel.clear();
		achievementLevel.sendKeys("0");


		WebElement submitBtn = webDriver.findElement(
				By.cssSelector("*[type='submit'].btn-primary"));

		//スクロールしてボタンを視界に入れる
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
		Thread.sleep(300);

		//強制クリック（物理クリックがブロックされるため）
		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", submitBtn);

		getEvidence(new Object() {},"test07");

		WebElement degreeAchieve = webDriver.findElement(By.id("content_0"));
		String className = degreeAchieve.getAttribute("class");

		assertTrue(className.contains("errorInput"));
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() throws InterruptedException {
		// TODO ここに追加
		WebElement achievementLevel = webDriver.findElement(
				By.id("content_0"));

		WebElement syokan = webDriver.findElement(
				By.id("content_1"));

		achievementLevel.clear();
		syokan.clear();


		WebElement submitBtn = webDriver.findElement(
				By.cssSelector("*[type='submit'].btn-primary"));

		//スクロールしてボタンを視界に入れる
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
		Thread.sleep(300);

		//強制クリック（物理クリックがブロックされるため）
		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", submitBtn);

		getEvidence(new Object() {},"test08");

		WebElement degreeAchieve = webDriver.findElement(By.id("content_0"));
		WebElement degreeSyokan = webDriver.findElement(By.id("content_1"));
		String className = degreeAchieve.getAttribute("class");
		String className2 = degreeAchieve.getAttribute("class");

		assertTrue(className.contains("errorInput"));
		assertTrue(className2.contains("errorInput"));
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

		WebElement syokan = webDriver.findElement(By.id("content_1"));
		WebElement weeklyReview  = webDriver.findElement(By.id("content_2"));

		String overTwoThousand = "a".repeat(2001);

		syokan.clear();
		syokan.sendKeys(overTwoThousand);

		weeklyReview.clear();
		weeklyReview.sendKeys(overTwoThousand);

		WebElement submitBtn = webDriver.findElement(
				By.cssSelector("*[type='submit'].btn-primary"));

		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
		Thread.sleep(300);

		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", submitBtn);

		wait.until(ExpectedConditions.attributeContains(By.id("content_1"), "class", "errorInput"));
		wait.until(ExpectedConditions.attributeContains(By.id("content_2"), "class", "errorInput"));

		getEvidence(new Object(){}, "test10");


		WebElement overSyokan   = webDriver.findElement(By.id("content_1"));
		WebElement overWeekReview = webDriver.findElement(By.id("content_2"));

		assertTrue(overSyokan.getAttribute("class").contains("errorInput"));
		assertTrue(overWeekReview.getAttribute("class").contains("errorInput"));
	}


}
