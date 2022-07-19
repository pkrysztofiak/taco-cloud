package tacos.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.TacoOrder;
import tacos.data.IngredientRepository;
import tacos.Ingredient.Type;
import tacos.Taco;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

	private final IngredientRepository ingredientRepository;

	public DesignTacoController(IngredientRepository ingredientRepository) {
		this.ingredientRepository = ingredientRepository;
	}

	@ModelAttribute
	public void addIngredientsToModel(Model model) {
		Iterable<Ingredient> ingredients = ingredientRepository.findAll();

		Type[] types = Ingredient.Type.values();

		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
	}

	private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
		return StreamSupport.stream(ingredients.spliterator(), false).filter(i -> i.getType().equals(type))
				.collect(Collectors.toList());
	}

	@ModelAttribute(name = "tacoOrder")
	public TacoOrder order() {
		log.info("order()");
		return new TacoOrder();
	}

	@ModelAttribute(name = "taco")
	public Taco taco() {
		log.info("taco()");
		return new Taco();
	}

	@GetMapping
	public String showDesignForm() {
		log.info("showDesignForm()");
		return "design";
	}

	@PostMapping
	public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
		if (errors.hasErrors()) {
			return "design";
		}

		tacoOrder.addTaco(taco);
		log.info("Processing taco: {}", taco);

		return "redirect:/orders/current";
	}
}
