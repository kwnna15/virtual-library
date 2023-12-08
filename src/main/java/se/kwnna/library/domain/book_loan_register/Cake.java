package se.kwnna.library.domain.book_loan_register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Cake {
    private final Integer sugarInGrams;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer sugarInGrams;

        private Builder() {
        }

        public Builder withSugarInGrams(Integer sugarInGrams) {
            this.sugarInGrams = sugarInGrams;
            return this;
        }

        public Cake build() {
            return new Cake(sugarInGrams);
        }
    }
}
