
Aplikacja umożliwiająca zakup towarów.
Podstawowe założenia:
1. Produkty powinny zawierać: Id, nazwę, opis
2. Do magazynu można dodawać jedynie produkty, które zostały przez nas zdefiniowane
3. Możemy dokonać zakupu dostępnych towarów.
Aplikacja ma udostępniać trzy endpointy:
1. `/product’ - zarządzanie obsługiwanymi produktami:
• Dodawanie nowego produktu;
• Usuwanie istniejącego produktu;
• Modyfikowanie istniejącego produktu;
2. `/inventory` - zarządzanie stanem magazynu
• POST - Dodawanie produktów do magazynu zadaną, w parametrze count, liczbę
produktów o identyfikatorze code. Jeśli w magazynie są już takie produkty, ich liczba
jest zwiększana o zadaną ilość. Obydwie wartości są wymagane i 0 < count < 1000. W
przypadku błędu, endpoint odpowiada odpowiednią informacją.
Przykładowy JSON:
{
„code”: „XXX”,
„count”: 50
}

• GET – Lista kodów produktów i ich liczba dostępnych sztuk (jedynie produkty, których
stan magazynowy jest dodatni)
3. `/purchase`
• POST
Przykładowy JSON:
{
„XXX”: 5,
„YZD”: 7
}
Reguły walidacji:
• co najmniej jedna pozycja do zakupu
• id pozycji musi być poprawny
• ilość produktów w zakresie 0, 1000
W przypadku błędu należy odpowiedzieć odpowiednią informacją.
W przypadku poprawnych parametrów, oraz jeśli w magazynie jest wystarczająca ilość
WSZYSTKICH produktów, aplikacja powinna zmniejszyć ilość dostępnych produktów
każdego typu i odpowiedzieć podsumowaniem zakupów:
{
„success”: true,
„purchasedProducts”:{
„XXX”: 5,
„YZD”: 7
}
}
• W przypadku poprawnych parametrów oraz gdy w magazynie nie ma wystarczającej ilości
CO NAJMNIEJ JEDNEGO produktu, aplikacja NIE zmniejsza dostępności żadnego z

produktów i odpowiada podsumowaniem. W podsumowaniu wyświetlamy informację,
których produktów brakuje.
{
„success”: false,
„missingProducts”:{
„XXX”: 5, (prezentujemy żądaną liczbę produktów, a nie liczbę brakujących)
}
}

ROZWINIĘCIE APLIKACJI:
• Dodać cenę jednostkową produktu. W przypadku skutecznego zamówienia produktów
dodać łączny koszt zamówienia, przy czym jeśli przekroczy on 200 zł należy zastosować
5% rabatu.
• Dodać możliwość dodawania ceny jednostkowej produktu w różnych walutach (PLN, USD,
EUR). Wartość zamówienia powinna być zwracana w PLN więc pozostałe waluty powinny
mieć możliwość przeliczania na PLN – zbadaj możliwość zastosowania darmowej,
zewnętrznej usługi do odczytywania kursu walut np. (https://rapidapi.com/auth/sign-up?
referral=/natkapral/api/currency-converter5)
