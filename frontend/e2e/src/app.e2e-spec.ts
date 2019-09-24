import { AppPage } from './app.po';
import { browser, logging } from 'protractor';

// @ts-ignore
describe('workspace-project App', () => {
  let page: AppPage;


  // @ts-ignore
  beforeEach(() => {
    page = new AppPage();
  });


  // @ts-ignore
  it('should display welcome message', () => {
    page.navigateTo();
    // @ts-ignore
    expect(page.getTitleText()).toEqual('Welcome to frontend!');
  });


  // @ts-ignore
  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);

    // @ts-ignore
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    }));
  });
});
