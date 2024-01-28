package MoneyCalculator;

import MoneyCalculator.Fixerws.FixerExchangeRateLoader;

public class ExchangeMoneyCommand implements Command{
    private final MoneyDialog moneyDialog;
    private final CurrencyDialog currencyDialog;
    private final FixerExchangeRateLoader fixerExchangeRateLoader;
    private final MoneyDisplay moneyDisplay;

    public ExchangeMoneyCommand(MoneyDialog moneyDialog, CurrencyDialog currencyDialog, FixerExchangeRateLoader fixerExchangeRateLoader, MoneyDisplay moneyDisplay) {
        this.moneyDialog = moneyDialog;
        this.currencyDialog = currencyDialog;
        this.fixerExchangeRateLoader = fixerExchangeRateLoader;
        this.moneyDisplay = moneyDisplay;
    }

    @Override
    public void execute() {
        Money money = moneyDialog.get();
        Currency currency = currencyDialog.get();

        ExchangeRate exchangeRate = fixerExchangeRateLoader.load(money.currency(), currency);
        Money result = new Money(money.amount()*exchangeRate.rate(), currency);

        moneyDisplay.show(result);
    }
}
