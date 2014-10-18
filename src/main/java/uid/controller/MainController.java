package uid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	MainController() {

	}

	/**
	 * For index
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getPages() {

		ModelAndView model = new ModelAndView("index");
		return model;

	}

	/**
	 * for main?algorithm=...
	 * @param modelMap
	 * @param algorithm
	 * @return
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main(ModelMap modelMap,
			@RequestParam String algorithmName) {

		modelMap.addAttribute("algorithmName", algorithmName);
		ModelAndView model = new ModelAndView("main");

		return model;

	}

}