# IndieTrip

Aplicativo Android desenvolvido para a atividade da disciplina de Programação para Dispositivos Móveis. O projeto funciona como um assistente simples de planejamento de viagens e possui quatro Activities integradas.

## Fluxo do aplicativo

1. **Configuração da viagem**
   - Seleção de país, estado e cidade.
   - Escolha das datas de partida e retorno.
   - Seleção das preferências da viagem.

2. **Seleção de atividades**
   - Exibe atividades relacionadas às preferências escolhidas.
   - Permite selecionar uma ou mais opções em uma lista personalizada.

3. **Detalhes da atividade**
   - Permite ajustar duração, dificuldade, horário e quantidade de pessoas.
   - Possui a opção de adicionar um lembrete de equipamentos.

4. **Resumo da viagem**
   - Apresenta destino, período, duração e preferências.
   - Lista as atividades selecionadas e seus detalhes.
   - Calcula a quantidade de atividades e o tempo total planejado.
   - Permite voltar para editar as atividades ou finalizar o planejamento.

Os dados são transportados entre as telas com `Intent` e impressos no Logcat em cada transição usando a tag `IndieTrip`.

## Componentes utilizados

- `Activity`
- `Intent`
- `AutoCompleteTextView`
- `DatePickerDialog`
- `TimePickerDialog`
- `CheckBox`
- `RadioGroup` e `RadioButton`
- `SeekBar`
- `ListView` e `BaseAdapter`
- `Button`, `TextView`, `EditText` e `ImageView`
- Layouts e drawables XML nativos

## Arquivos principais

```text
app/src/main/
├── assets/
│   ├── countries.json
│   ├── states.json
│   └── cities.json
├── java/com/example/indietrip/
│   ├── MainActivity.kt
│   ├── LocationSelector.kt
│   ├── LocationDataSource.kt
│   ├── DateSelector.kt
│   ├── ActivitySelectionActivity.kt
│   ├── ActivityDataSource.kt
│   ├── ActivityOption.kt
│   ├── ActivityOptionAdapter.kt
│   ├── ActivityDetailsActivity.kt
│   ├── TripSummaryActivity.kt
│   ├── PlannedActivity.kt
│   ├── TripSummaryAdapter.kt
│   └── TripExtras.kt
└── res/
    ├── drawable/
    ├── drawable-nodpi/
    ├── layout/
    └── values/
```

- `MainActivity`: formulário inicial da viagem.
- `LocationSelector` e `LocationDataSource`: carregamento e filtro das localizações.
- `DateSelector`: seleção e validação das datas.
- `ActivitySelectionActivity`: lista e seleção das atividades.
- `ActivityDetailsActivity`: configuração da atividade escolhida.
- `TripSummaryActivity`: resumo e finalização do planejamento.
- `TripExtras`: nomes das chaves utilizadas na passagem de dados.
- `ActivityOptionAdapter` e `TripSummaryAdapter`: adapters das listas personalizadas.

## Imagens

O cabeçalho utiliza `indietrip_header.png` e `indietrip_header_right.png` em `res/drawable-nodpi`.

As ilustrações das atividades:

- `activity_trail`
- `activity_museum`
- `activity_picnic`
- `activity_camping`
- `activity_beach`

## Executando o projeto

Abra o projeto no Android Studio, aguarde a sincronização do Gradle e execute o módulo `app` em um dispositivo ou emulador com Android 9 ou superior.