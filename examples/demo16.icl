def glo = new(0) in
	def f = fun n:Int -> glo := *glo + n end in
		f(2);
		f(3);
		f(4);
		println *glo
	end
end ;;